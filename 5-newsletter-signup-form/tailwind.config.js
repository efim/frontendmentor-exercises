const defaultTheme = require('tailwindcss/defaultTheme')

/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./index.html",
    "./src/**/*.scala",
  ],
  theme: {
    colors: {
      tomato: '#FF6257',
      'grey-dark-slate': '#2F3356',
      'grey-charcoal': '#36384E',
      grey: '#363A4E',
      white: '#FFFFFF',
      'button-left': '#FF5478',
      'button-right': '#FF673F',
      'input-error': '#FFE8E6',
      'dark-bg': '#36384D',
    },
    extend: {
      fontFamily: {
        'custom': ['Roboto', ...defaultTheme.fontFamily.sans],
      },
    },
  },
  plugins: [],
}
