console.log("cart.js loaded");

// ── Coupon definitions ──────────────────────────────────────────
const COUPONS = {
  SAVE5:  { code: "SAVE5",  percent: 5,  label: "SAVE5 — 5% off" },
  FLAT10: { code: "FLAT10", percent: 10, label: "FLAT10 — 10% off" },
  OFF15:  { code: "OFF15",  percent: 15, label: "OFF15 — 15% off" },
  NEW20:  { code: "NEW20",  percent: 20, label: "NEW20 — 20% off" },
};

let activeCoupon = null; // currently applied coupon key
let lastServerData = null; // cache last order summary from server

// ── Update cart via AJAX ──────────────────────────────────────────
async function updateCart(itemId, action) {
  console.log("updateCart:", itemId, action);

  try {
    const response = await fetch("/api/cart/update", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      credentials: "include",
      body: JSON.stringify({ itemId, action }),
    });

    if (!response.ok) {
      console.error("Error updating cart:", await response.text());
      return;
    }

    const data = await response.json();
    lastServerData = data;

    // Update the navbar cart badge with bounce
    updateCartCounter(data.itemCount);

    if (action === "remove") {
      removeCartItem(itemId);
      updateOrderSummary(data);
    } else {
      // Use server-returned itemQuantity (source of truth)
      updateHomeProductDisplay(itemId, data.itemQuantity);
      updateCartPageItem(itemId, data);
    }
  } catch (error) {
    console.error("Error in updateCart:", error);
  }
}

// ── Home page: toggle Add-to-Cart button ↔ quantity controls ──
function updateHomeProductDisplay(itemId, serverQty) {
  const productItem = document.querySelector(
    `.product-item[data-product-id="${itemId}"]`
  );
  if (!productItem) return;

  const addButton = productItem.querySelector(".add-to-cart-button");
  const quantityControls = productItem.querySelector(".quantity-controls");
  if (!addButton || !quantityControls) return;

  const qtyDisplay = quantityControls.querySelector(".qty-display");

  if (serverQty > 0) {
    addButton.style.display = "none";
    quantityControls.style.display = "flex";
    qtyDisplay.textContent = serverQty;
  } else {
    addButton.style.display = "inline-flex";
    quantityControls.style.display = "none";
    qtyDisplay.textContent = "0";
  }
}

// ── Cart page: update quantity, subtotal, and order summary ──
function updateCartPageItem(itemId, data) {
  const cartItem = document.getElementById(`cart-item-${itemId}`);
  if (!cartItem) return;

  const qtyInput = cartItem.querySelector(".item-qty");
  const subtotalEl = cartItem.querySelector(".item-subtotal");
  const unitPrice = parseFloat(cartItem.dataset.unitPrice) || 0;

  if (data.itemQuantity <= 0) {
    removeCartItem(itemId);
    updateOrderSummary(data);
    return;
  }

  // Set quantity from server
  if (qtyInput) qtyInput.value = data.itemQuantity;

  // Update subtotal (unit price * quantity)
  if (subtotalEl) {
    subtotalEl.textContent = "$" + (unitPrice * data.itemQuantity).toFixed(2);
  }

  // Update order summary from API response
  updateOrderSummary(data);
}

// ── Order summary: update all fields, applying coupon if active ──
function updateOrderSummary(data) {
  lastServerData = data;

  const originalPrice = data.originalPrice || 0;
  const savings = data.savings || 0;
  const discountedPrice = originalPrice - savings;

  // Apply coupon on discounted price
  let couponDiscount = 0;
  if (activeCoupon && COUPONS[activeCoupon]) {
    couponDiscount = discountedPrice * (COUPONS[activeCoupon].percent / 100);
  }
  const afterCoupon = discountedPrice - couponDiscount;

  // Recalculate tax and delivery based on after-coupon price
  const tax = afterCoupon * 0.19;
  const delivery = afterCoupon > 0 && afterCoupon < 500 ? 100.0 : 0.0;
  const grandTotal = afterCoupon + tax + delivery;

  // Update DOM
  setField("original-price", originalPrice);
  setField("savings", savings);
  setField("tax", tax);
  setField("total", grandTotal);

  // Delivery
  const deliveryEl = document.querySelector('[data-summary="delivery"]');
  if (deliveryEl) {
    deliveryEl.textContent = delivery > 0 ? "$" + delivery.toFixed(2) : "Free";
  }

  // Coupon discount row
  const couponRow = document.getElementById("coupon-discount-row");
  const couponEl = document.querySelector('[data-summary="coupon-discount"]');
  if (couponRow && couponEl) {
    if (couponDiscount > 0) {
      couponRow.style.display = "flex";
      couponEl.textContent = "-$" + couponDiscount.toFixed(2);
    } else {
      couponRow.style.display = "none";
    }
  }
}

function setField(key, value) {
  const el = document.querySelector(`[data-summary="${key}"]`);
  if (el && value !== undefined) {
    el.textContent = "$" + value.toFixed(2);
  }
}

// ── Cart badge with bounce animation ──
function updateCartCounter(count) {
  const cartCounter = document.getElementById("cart-counter");
  if (!cartCounter) return;

  cartCounter.textContent = count;
  cartCounter.style.display = count > 0 ? "inline-flex" : "none";

  // Trigger bounce animation
  cartCounter.classList.remove("badge-bounce");
  void cartCounter.offsetWidth; // force reflow
  cartCounter.classList.add("badge-bounce");
}

// ── Cart item removal with fade animation ──
function removeCartItem(itemId) {
  const cartItem = document.getElementById(`cart-item-${itemId}`);
  if (!cartItem) return;

  cartItem.style.transition = "all 0.3s ease-out";
  cartItem.style.opacity = "0";
  cartItem.style.transform = "translateX(20px)";

  setTimeout(() => {
    cartItem.remove();

    const remainingItems = document.querySelectorAll('[id^="cart-item-"]');
    if (remainingItems.length === 0) {
      showEmptyCartMessage();
    }
  }, 300);
}

function showEmptyCartMessage() {
  const cartContainer = document.querySelector(".space-y-6");
  if (!cartContainer) return;

  cartContainer.innerHTML = `
    <div class="text-center py-12">
      <i class="fa-solid fa-cart-shopping text-gray-300 dark:text-gray-600 text-5xl mb-4"></i>
      <p class="text-gray-500 dark:text-gray-400 text-lg">Your cart is empty</p>
      <a href="/store/home"
         class="inline-flex items-center gap-2 text-blue-600 hover:underline mt-4 font-medium">
        Continue Shopping
        <svg class="h-5 w-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 8l4 4m0 0l-4 4m4-4H3"/>
        </svg>
      </a>
    </div>
  `;
}

// ── Coupon functions ──────────────────────────────────────────
function selectCoupon(code) {
  const input = document.getElementById("coupon-input");
  if (input) input.value = code;
  applyCoupon(code);
}

function applyCouponFromInput() {
  const input = document.getElementById("coupon-input");
  if (!input) return;
  const code = input.value.trim().toUpperCase();
  if (!code) return;
  applyCoupon(code);
}

function applyCoupon(code) {
  const errorEl = document.getElementById("coupon-error");
  const bannerEl = document.getElementById("active-coupon-banner");
  const bannerText = document.getElementById("active-coupon-text");

  // Hide previous error
  if (errorEl) errorEl.classList.add("hidden");

  // Validate coupon
  if (!COUPONS[code]) {
    if (errorEl) {
      errorEl.textContent = `"${code}" is not a valid coupon code.`;
      errorEl.classList.remove("hidden");
    }
    return;
  }

  // Set active coupon
  activeCoupon = code;

  // Update input
  const input = document.getElementById("coupon-input");
  if (input) input.value = code;

  // Show active banner
  if (bannerEl && bannerText) {
    bannerText.textContent = COUPONS[code].label + " applied";
    bannerEl.classList.remove("hidden");
    bannerEl.style.display = "flex";
  }

  // Highlight active card, deactivate others
  document.querySelectorAll(".coupon-card").forEach((card) => {
    if (card.dataset.coupon === code) {
      card.classList.add("active");
    } else {
      card.classList.remove("active");
    }
  });

  // Recalculate order summary with coupon
  if (lastServerData) {
    updateOrderSummary(lastServerData);
  }
}

function removeCoupon() {
  activeCoupon = null;

  // Clear input
  const input = document.getElementById("coupon-input");
  if (input) input.value = "";

  // Hide banner
  const bannerEl = document.getElementById("active-coupon-banner");
  if (bannerEl) {
    bannerEl.classList.add("hidden");
    bannerEl.style.display = "";
  }

  // Hide error
  const errorEl = document.getElementById("coupon-error");
  if (errorEl) errorEl.classList.add("hidden");

  // Deactivate all cards
  document.querySelectorAll(".coupon-card").forEach((card) => {
    card.classList.remove("active");
  });

  // Recalculate without coupon
  if (lastServerData) {
    updateOrderSummary(lastServerData);
  }
}

// ── Initialize lastServerData from page on load ──
document.addEventListener("DOMContentLoaded", () => {
  const origEl = document.querySelector('[data-summary="original-price"]');
  const savingsEl = document.querySelector('[data-summary="savings"]');
  const taxEl = document.querySelector('[data-summary="tax"]');
  const deliveryEl = document.querySelector('[data-summary="delivery"]');
  const totalEl = document.querySelector('[data-summary="total"]');

  if (origEl) {
    lastServerData = {
      originalPrice: parseFloat(origEl.textContent.replace("$", "")) || 0,
      savings: parseFloat((savingsEl && savingsEl.textContent.replace("$", "")) || 0),
      tax: parseFloat((taxEl && taxEl.textContent.replace("$", "")) || 0),
      delivery: deliveryEl && deliveryEl.textContent !== "Free"
        ? parseFloat(deliveryEl.textContent.replace("$", "")) || 0
        : 0,
      grandTotal: parseFloat((totalEl && totalEl.textContent.replace("$", "")) || 0),
    };
  }
});
