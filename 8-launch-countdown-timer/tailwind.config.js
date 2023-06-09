/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.scala",
  ],
  theme: {
    extend: {
      fontFamily: {
        'inter': ['Red Hat Text', 'sans-serif'],
      },
      backgroundImage: {
        'pattern-hills': "url('/images/pattern-hills.svg')",
        'pattern-stars': "url('/images/bg-stars.svg')",
      },
    },
    colors: {
      primary: {
        blue: 'hsl(237, 18%, 59%)',
        red: 'hsl(345, 95%, 68%)',
      },
      neutral: {
        white: 'hsl(0, 0%, 100%)',
        'desaturated-blue': 'hsl(236, 21%, 26%)',
        'dark-blue': 'hsl(235, 16%, 14%)',
        'black-blue': 'hsl(234, 17%, 12%)',
      },
      clock: {
        top: '#2C2C44',
        bottom: '#32374E',
        text: '#8B889E',
      },
    },
  },
  plugins: [],
}

