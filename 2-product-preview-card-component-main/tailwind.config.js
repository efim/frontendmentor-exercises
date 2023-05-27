/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.scala",
  ],
  theme: {
    extend: {
      colors: {
        'dark-cyan': '#3C8067',
        'darkest-cyan': '#1A4031',
        'cream': '#F2EBE3',
      },
    },
    fontFamily: {
      'sans': ['Montserrat', 'sans-serif'],
      'serif': ['Fraunces', 'serif'],
    },
  },
  plugins: [],
}

