function toggleReviewForm() {
  const reviewForm = document.querySelector(".review-form");
  if (reviewForm) {
    reviewForm.classList.toggle("hidden");
  }
}

function handleReviewSubmission(event) {
  event.preventDefault();
  const form = event.target;
  const formData = new FormData(form);

  fetch(form.action, {
    method: "POST",
    body: formData,
  }).then((response) => {
    if (response.ok) {
      // Hide the form after successful submission
      const reviewForm = document.querySelector(".review-form");
      if (reviewForm) {
        reviewForm.classList.add("hidden");
      }
      // Reload the page to show the new review
      window.location.reload();
    }
  });
}
