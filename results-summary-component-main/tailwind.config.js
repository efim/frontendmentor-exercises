/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.scala",
  ],
  theme: {
    extend: {
      fontFamily: {
        'sans': ['Lato', 'sans-serif'], // This will set Roboto as the default sans font
      },
      fontWeight: {
        'thin': 100,
        'normal': 400,
        'semibold': 600,
        'bold': 700,
        'extrabold': 900,
      },
      boxShadow: {
        'custom': '0 25px 60px 2px rgba(69, 130, 255, 0.2)',
      },
    }
  },
  plugins: [],
}

