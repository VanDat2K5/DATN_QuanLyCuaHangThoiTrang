package com.poly.service;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.poly.model.CartItem;
import com.poly.model.ChatItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// import java.util.HashMap;
import java.util.Map;

@Service
public class CartService {

    private final FirebaseDatabase firebaseDatabase;

    @Autowired
    public CartService(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    // Thêm sản phẩm vào giỏ hàng
    public void addToCart(String userId, String productId, int quantity, double price) {
        DatabaseReference cartRef = firebaseDatabase.getReference("carts").child(userId).child("items")
                .child(productId);

        CartItem item = new CartItem();
        item.setIdSPCT(productId);
        item.setSoLuong(quantity);
        item.setGia((int) price);

        cartRef.setValue(item, (error, ref) -> {
            if (error != null) {
                System.err.println("Lỗi khi thêm vào giỏ hàng: " + error.getMessage());
            }
        });
    }

    // Cập nhật số lượng sản phẩm
    public void updateQuantity(String userId, String productId, int quantity) {
        DatabaseReference cartRef = firebaseDatabase.getReference("carts").child(userId).child("items")
                .child(productId);
        cartRef.child("quantity").setValue(quantity, (error, ref) -> {
            if (error != null) {
                System.err.println("Lỗi khi cập nhật số lượng: " + error.getMessage());
            }
        });
    }

    // Cập nhật giá sản phẩm (được gọi khi giá trong database thay đổi)
    public void updatePrice(String userId, String productId, double newPrice) {
        DatabaseReference cartRef = firebaseDatabase.getReference("carts").child(userId).child("items")
                .child(productId);
        cartRef.child("price").setValue(newPrice, (error, ref) -> {
            if (error != null) {
                System.err.println("Lỗi khi cập nhật giá: " + error.getMessage());
            }
        });
    }

    // Xóa sản phẩm khỏi giỏ hàng
    public void removeFromCart(String userId, String productId) {
        DatabaseReference cartRef = firebaseDatabase.getReference("carts").child(userId).child("items")
                .child(productId);
        cartRef.removeValue((error, ref) -> {
            if (error != null) {
                System.err.println("Lỗi khi xóa khỏi giỏ hàng: " + error.getMessage());
            }
        });
    }

    // Lấy tổng tiền giỏ hàng
    public void getCartTotal(String userId, CartTotalCallback callback) {
        DatabaseReference cartRef = firebaseDatabase.getReference("carts").child(userId).child("items");

        cartRef.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                double total = 0.0;
                if (snapshot.exists()) {
                    for (var item : snapshot.getChildren()) {
                        CartItem cartItem = item.getValue(CartItem.class);
                        if (cartItem != null) {
                            total += cartItem.getGia() * cartItem.getSoLuong();
                        }
                    }
                }
                callback.onTotalCalculated(total);
            }

            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError error) {
                System.err.println("Lỗi khi tính tổng tiền: " + error.getMessage());
                callback.onTotalCalculated(0.0);
            }
        });
    }

    // Interface callback cho việc tính tổng tiền
    public interface CartTotalCallback {
        void onTotalCalculated(double total);
    }

    // Đồng bộ giá cho tất cả sản phẩm trong giỏ hàng
    public void syncPrices(String userId, Map<String, Double> newPrices) {
        DatabaseReference cartRef = firebaseDatabase.getReference("carts").child(userId).child("items");

        cartRef.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (var item : snapshot.getChildren()) {
                        CartItem cartItem = item.getValue(CartItem.class);
                        if (cartItem != null && newPrices.containsKey(cartItem.getIdSPCT())) {
                            // Cập nhật giá mới nếu có trong danh sách
                            updatePrice(userId, cartItem.getIdSPCT(), newPrices.get(cartItem.getIdSPCT()));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError error) {
                System.err.println("Lỗi khi đồng bộ giá: " + error.getMessage());
            }
        });
    }

    public void sendMessage(String maKH, ChatItem message) {
        DatabaseReference chatRef = firebaseDatabase.getReference("ChatBox");
        chatRef.orderByChild("MaKH").equalTo(maKH)
                .addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                        for (com.google.firebase.database.DataSnapshot chatSnapshot : snapshot.getChildren()) {
                            DatabaseReference itemsRef = chatSnapshot.getRef().child("items");
                            itemsRef.push().setValue(message, (error, ref) -> {
                                if (error != null) {
                                    System.err.println("Lỗi khi gửi tin nhắn: " + error.getMessage());
                                }
                            });
                            break;
                        }
                    }

                    @Override
                    public void onCancelled(com.google.firebase.database.DatabaseError error) {
                        System.err.println("Lỗi khi gửi tin nhắn: " + error.getMessage());
                    }
                });
    }
}