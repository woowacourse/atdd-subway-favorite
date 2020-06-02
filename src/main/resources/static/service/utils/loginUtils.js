export const getCookie = function () {
  const value = document.cookie.match('(^|;) ?token=([^;]*)(;|$)');
  return value ? value[2] : null;
};

export const deleteCookie = function () {
  document.cookie = 'token=; expires=Thu, 01 Jan 1999 00:00:10 GMT;';
}

export const setCookie = function (value) {
  const date = new Date();
  date.setTime(date.getTime() + 5 * 60 * 1000);
  document.cookie = "token=" + value + ';expires=' + date.toUTCString();
};

