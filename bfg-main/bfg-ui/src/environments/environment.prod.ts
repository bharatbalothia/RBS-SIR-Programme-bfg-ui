export const environment = {
  production: true,
  apiUrl: window.location.origin + '/' + (window.location.pathname.split('/')[1] || '') + '/api/'
};
