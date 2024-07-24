const { override, addWebpackAlias } = require('customize-cra');
const path = require('path');

module.exports = override(
    addWebpackAlias({
        '@assets': path.resolve(__dirname, 'src/assets'),
        '@components': path.resolve(__dirname, 'src/components'),
        '@styles': path.resolve(__dirname, 'src/assets/styles'),
        '@stores': path.resolve(__dirname, 'src/store')
    })
);
