document.addEventListener("click", (e) => {
  if (e.target.matches(".toggle-sidebar-btn")) {
    document.getElementById("body").classList.toggle("toggle-sidebar");
  }

  if (e.target.matches("#btn-logout *")) {
    document.getElementById("btn-form-logout").click();
  }
});
