var images = [];

images[0] = ['photoFromInternet'];
images[1] = ['photoFromInternet2'];
var index = 0;

function change() {
  document.getElementById("mainPhoto").src = images[index];
  if (index == 1) {
    index = 0;
  } else {
    index++;
  }

  setTimeout(change, 3000);
}

window.onload = change();