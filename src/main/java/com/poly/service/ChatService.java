package com.poly.service;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.poly.model.ChatItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ChatService {

    private final FirebaseDatabase firebaseDatabase;

    @Autowired
    public ChatService(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    // Gửi tin nhắn mới
    public void sendMessage(String userId, String role, String text) {
        DatabaseReference chatRef = firebaseDatabase.getReference("chats").child(userId).child("items");

        ChatItem message = new ChatItem();
        message.setVaiTro(role.equals("cus"));
        message.setNoiDung(text);
        message.setThoiGian(Instant.now().toEpochMilli());
        message.setVaiTro(role.equals("cus"));

        // Thêm tin nhắn mới vào cuối mảng
        chatRef.push().setValue(message, (error, ref) -> {
            if (error != null) {
                System.err.println("Lỗi khi gửi tin nhắn: " + error.getMessage());
            }
        });
    }

    // Đánh dấu tin nhắn đã đọc
    public void markAsSeen(String userId, String messageId) {
        DatabaseReference messageRef = firebaseDatabase.getReference("chats")
                .child(userId)
                .child("items")
                .child(messageId)
                .child("seen");

        messageRef.setValue(true, (error, ref) -> {
            if (error != null) {
                System.err.println("Lỗi khi đánh dấu tin nhắn đã đọc: " + error.getMessage());
            }
        });
    }

    // Đánh dấu tất cả tin nhắn đã đọc
    public void markAllAsSeen(String userId) {
        DatabaseReference chatRef = firebaseDatabase.getReference("chats").child(userId).child("items");

        chatRef.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (var messageSnapshot : snapshot.getChildren()) {
                        messageSnapshot.child("seen").getRef().setValue(true, (error, ref) -> {
                            if (error != null) {
                                System.err.println("Lỗi khi đánh dấu tin nhắn đã đọc: " + error.getMessage());
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError error) {
                System.err.println("Lỗi khi đánh dấu tất cả tin nhắn đã đọc: " + error.getMessage());
            }
        });
    }

    // Lắng nghe tin nhắn mới
    public void listenToMessages(String userId, MessageListener listener) {
        DatabaseReference chatRef = firebaseDatabase.getReference("chats").child(userId).child("items");

        chatRef.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (var messageSnapshot : snapshot.getChildren()) {
                        ChatItem message = messageSnapshot.getValue(ChatItem.class);
                        if (message != null) {
                            listener.onMessageReceived(message);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError error) {
                System.err.println("Lỗi khi lắng nghe tin nhắn: " + error.getMessage());
            }
        });
    }

    // Đếm số tin nhắn chưa đọc
    public void countUnreadMessages(String userId, UnreadCountListener listener) {
        DatabaseReference chatRef = firebaseDatabase.getReference("chats").child(userId).child("items");

        chatRef.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                int unreadCount = 0;
                if (snapshot.exists()) {
                    for (var messageSnapshot : snapshot.getChildren()) {
                        Boolean seen = messageSnapshot.child("seen").getValue(Boolean.class);
                        if (seen != null && !seen) {
                            unreadCount++;
                        }
                    }
                }
                listener.onUnreadCountUpdated(unreadCount);
            }

            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError error) {
                System.err.println("Lỗi khi đếm tin nhắn chưa đọc: " + error.getMessage());
            }
        });
    }

    // Interface để lắng nghe tin nhắn mới
    public interface MessageListener {
        void onMessageReceived(ChatItem message);
    }

    // Interface để lắng nghe số tin nhắn chưa đọc
    public interface UnreadCountListener {
        void onUnreadCountUpdated(int count);
    }
}