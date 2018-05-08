$(document).ready(function () {
  $.ajax({
    type: "POST",
    url: "/getEmployees",
    success: function (data) {
      $.each(data, function (key,value) {
        $('#table').append('<tr><td>'+value+'</td></tr>')
      });
    }
  })
});