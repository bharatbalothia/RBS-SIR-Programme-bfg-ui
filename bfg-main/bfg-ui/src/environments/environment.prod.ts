export const environment = {
  production: true,
  apiUrl: window.location.origin + window.location.pathname.substring(0, window.location.pathname.indexOf('/', 2)) + '/api/'
};
