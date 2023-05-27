const defaultTheme = require('tailwindcss/defaultTheme')

/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./index.html",
    "./src/**/*.scala",
  ],
  theme: {
    extend: {
      fontFamily: {
        'custom': ['Roboto', ...defaultTheme.fontFamily.sans],
      },
    },
  },
  plugins: [],
}
