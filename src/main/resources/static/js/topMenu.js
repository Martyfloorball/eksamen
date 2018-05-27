$(document).ready(function () {
  var path = window.location.pathname;
  if(path == "/RKI/dashboard"){
    $("#menu ul li:nth-child(1) a").addClass("active");

  }else if(path == "/RKI/vagtplan"){
    $("#menu ul li:nth-child(2) a").addClass("active");

  }else if(path == "/RKI/medarbejderliste"){
    $("#menu ul li:nth-child(3) a").addClass("active");
  }
  console.log(path);
});

$.ajax({
    type: "POST",
    url: "/RKI/getUserName",
    success: function (data) {
        $("#dropdownName").prepend(data[0] + " " + data[1] + " ");
    }
});