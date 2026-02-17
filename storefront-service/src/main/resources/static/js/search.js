document.addEventListener("DOMContentLoaded", function () {
  const searchInput = document.getElementById("searchInput");
  const sortSelect = document.getElementById("sortSelect");
  const categorySelect = document.getElementById("categorySelect");
  let timeout = null;

  // Search with debounce
  searchInput?.addEventListener("input", function (e) {
    clearTimeout(timeout);
    timeout = setTimeout(() => {
      const searchTerm = e.target.value;
      const [sortBy, direction] = sortSelect.value.split(",");
      const categories = getSelectedCategories();
      fetchProducts(searchTerm, sortBy, direction, categories);
    }, 500);
  });

  // Sort
  sortSelect?.addEventListener("change", function (e) {
    const searchTerm = searchInput.value;
    const [sortBy, direction] = e.target.value.split(",");
    const categories = getSelectedCategories();
    fetchProducts(searchTerm, sortBy, direction, categories);
  });

  // Category
  categorySelect?.addEventListener("change", function (e) {
    const searchTerm = searchInput.value;
    const [sortBy, direction] = sortSelect.value.split(",");
    const categories = getSelectedCategories();
    fetchProducts(searchTerm, sortBy, direction, categories);
  });

  // Helper function to get selected categories
  function getSelectedCategories() {
    const checkboxes = document.querySelectorAll(
      '#categorySelect input[type="checkbox"]:checked'
    );
    return Array.from(checkboxes).map((checkbox) => checkbox.value);
  }

  async function fetchProducts(
    title = "",
    sortBy = "itemId",
    direction = "asc",
    categories = []
  ) {
    try {
      // For category view
      let url;
      if (categories.length > 0) {
        url = new URL("/store/home/by-category-view", window.location.origin);
        categories.forEach((category) => {
          url.searchParams.append("categories", category);
        });
      } else {
        // For search view
        url = new URL("/store/home/search-view", window.location.origin);
      }

      // Add common parameters
      url.searchParams.append("title", title);
      url.searchParams.append("sortBy", sortBy);
      url.searchParams.append("direction", direction);
      url.searchParams.append("page", "0");
      url.searchParams.append("size", "20");

      const response = await fetch(url);
      const html = await response.text();

      // Find the products grid and update it
      const productsGrid = document.getElementById("products-grid");
      if (productsGrid) {
        const temp = document.createElement("div");
        temp.innerHTML = html;

        const newContent = temp.querySelector("#products-grid");
        if (newContent) {
          productsGrid.innerHTML = newContent.innerHTML;
        } else {
          console.error("Could not find products grid in response");
        }
      }

      // Update URL without page reload
      const newUrl = `/store/home?${url.searchParams.toString()}`;
      window.history.pushState({}, "", newUrl);
    } catch (error) {
      console.error("Error fetching products:", error);
    }
  }
});
