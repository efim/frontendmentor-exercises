/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.scala",
  ],
  theme: {
    extend: {
      colors: {
        orange: {
          DEFAULT: '#FB7413',
        },
        gray: {
          light: '#959EAC',
          medium: '#7C8798',
          dark: '#262D37',
        },
        blue: {
          dark: '#1E252F',
          'very-dark': '#171E28',
        }
      },
    },
  },
  plugins: [],
}

