/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.scala",
  ],
  theme: {
    extend: {
      colors: {
        'very-dark-gray': 'hsl(0, 0%, 17%)',
        'dark-gray': 'hsl(0, 0%, 59%)',
      },
      fontFamily: {
        'sans': ['Rubik', 'sans-serif'], // This will set Roboto as the default sans font
      },
      fontWeight: {
        'normal': 400,
        'semibold': 500,
        'bold': 700,
      }
    },
  },
  plugins: [],
}

