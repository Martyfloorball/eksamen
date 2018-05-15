$(document).ready(function () {
  var path = window.location.pathname;
  if(path == "/"){
    $("#menu ul li:nth-child(1) a").addClass("active");

  }else if(path == "/vagtplan"){
    $("#menu ul li:nth-child(2) a").addClass("active");

  }else if(path == "/medarbejderliste"){
    $("#menu ul li:nth-child(3) a").addClass("active");
  }
});

$.ajax({
    type: "POST",
    url: "/getUserName",
    success: function (data) {
        $("#dropdownName").prepend(data[0] + " " + data[1] + " ");
    }
});