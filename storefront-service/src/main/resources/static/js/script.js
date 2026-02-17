console.log("script loaded");

// Theme management
let currentTheme = getTheme();
applyTheme(currentTheme);

document.querySelector("#theme_change_button").addEventListener("click", () => {
  currentTheme = currentTheme === "dark" ? "light" : "dark";
  applyTheme(currentTheme);
});

function applyTheme(theme) {
  const html = document.querySelector("html");
  html.classList.remove("light", "dark");
  html.classList.add(theme);
  setTheme(theme);
}

function setTheme(theme) {
  localStorage.setItem("theme", theme);
}

function getTheme() {
  return localStorage.getItem("theme") || "light";
}

function togglePassword(id) {
  const input = document.getElementById(id);
  input.type = input.type === "password" ? "text" : "password";
}
