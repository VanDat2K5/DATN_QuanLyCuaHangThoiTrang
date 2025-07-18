const bgImages = [
  "https://images.unsplash.com/photo-1490481651871-ab68de25d43d", // Spring
  "https://images.unsplash.com/photo-1513104890138-7c749659a591", // Summer
  "https://images.unsplash.com/photo-1506744038136-46273834b3fb", // Autumn
  "https://images.unsplash.com/photo-1601597111024-11ee3fdb5aa3"  // Winter
];

const titles = ["Spring Collection", "Summer Heat", "Autumn Vibes", "Winter Warmth"];
const descriptions = [
  "Explore the latest trends in our vibrant spring collection.",
  "Stay cool and stylish this summer with our fresh arrivals.",
  "Get cozy with earthy tones and warm textures.",
  "Layer up with elegance in our winter picks."
];

function changePromo(index) {
  document.getElementById("promoBg").style.backgroundImage = `url(${bgImages[index]})`;
  document.getElementById("promoTitle").textContent = titles[index];
  document.getElementById("promoDescription").textContent = descriptions[index];
}

document.addEventListener("DOMContentLoaded", function() {
  changePromo(0);
});
  