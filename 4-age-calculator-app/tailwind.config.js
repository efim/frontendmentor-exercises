/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.scala",
  ],
  theme: {
    fontFamily: {
      'fancy-sans': ['Poppins', 'sans-serif']
    },
    fontWeight: {
      thinner: '400', // only italic
      medium: '700', // only bold
      thicker: '800', // on bold italic
    },
    extend: {
      colors: {
        'main-purple': '#854DFF',
        'light-red': '#FF5757',
        'off-white': '#F0F0F0',
        'light-grey': '#DBDBDB',
        'smokey-grey': '#716F6F',
        'off-black': '#141414',
      },
    },
  },
  plugins: [],
}

