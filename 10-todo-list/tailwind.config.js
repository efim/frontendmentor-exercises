/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.scala",
  ],
  theme: {
    extend: {
      fontFamily: {
        'sans': ['Josefin Sans', 'sans-serif'], // This will set Roboto as the default sans font
      },
      fontWeight: {
        'normal': 400,
        'bold': 700,
      },
      colors: {
        'primay-bright-blue': 'hsl(220, 98%, 61%)',
        'primary-check-blue': 'hsl(192, 100%, 67%)',
        'primary-check-purple': 'hsl(280, 87%, 65%)',
        // light theme
        'very-light-gray': 'hsl(0, 0%, 98%)',
        'very-light-grayish-blue': 'hsl(236, 33%, 92%)',
        'light-grayish-blue': 'hsl(233, 11%, 84%)',
        'dark-grayish-blue': 'hsl(236, 9%, 61%)',
        'very-dark-grayish-blue': 'hsl(235, 19%, 35%)',
        // dark theme
        'dt-very-dark-blue': 'hsl(235, 21%, 11%)',
        'dt-very-dark-desaturated-blue': 'hsl(235, 24%, 19%)',
        'dt-light-grayish-blue': 'hsl(234, 39%, 85%)',
        'dt-light-grayish-blue-hover': 'hsl(236, 33%, 92%)',
        'dt-dark-grayish-blue': 'hsl(234, 11%, 52%)',
        'dt-very-dark-grayish-blue': 'hsl(233, 14%, 35%)',
        'dt-very-dark-grayish-blue': 'hsl(237, 14%, 26%)',
      },
    },
  },
  plugins: [],
}

