/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.scala",
  ],
  theme: {
    extend: {
      'colors': {
        'moderate-blue': '#5457B6',
        'soft-red': '#ED6468',
        'light-grayish-blue': '#C3C4EF',
        'pale-red': '#FFB8BB',
        'dark-blue': '#324152',
        'grayish-blue': '#324152',
        'light-gray': '#324152',
        'very-light-gray': '#F5F6FA',
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

