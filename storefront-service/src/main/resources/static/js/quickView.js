let currentProductId = null;

function openQuickView(productId, image, title, description) {
  currentProductId = productId;
  const modal = document.getElementById("quickViewModal");

  // Update modal content
  document.getElementById("quickViewImage").src = image;
  document.getElementById("quickViewTitle").textContent = title;
  document.getElementById("quickViewDescription").textContent = description;

  // Show modal with fade effect
  modal.classList.remove("hidden");
  document.body.style.overflow = "hidden";
}

function closeQuickView() {
  const modal = document.getElementById("quickViewModal");
  modal.classList.add("hidden");
  document.body.style.overflow = "auto";
  currentProductId = null;
}

// Close on backdrop click
document.addEventListener("DOMContentLoaded", () => {
  const modal = document.getElementById("quickViewModal");
  modal.addEventListener("click", (e) => {
    if (e.target === modal) {
      closeQuickView();
    }
  });
});

// Close on ESC key
document.addEventListener("keydown", (e) => {
  if (e.key === "Escape") {
    closeQuickView();
  }
});