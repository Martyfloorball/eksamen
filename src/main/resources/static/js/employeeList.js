$(document).ready(function () {
  $.ajax({
    type: "POST",
    url: "/getEmployees",
     success: function (data) {
      console.log(data);
    }
  })
});