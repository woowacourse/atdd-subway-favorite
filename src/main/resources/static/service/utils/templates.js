export const listItemTemplate = value =>
  `<div class="list-item border border-gray-200 py-2 px-4 text-gray-800">
  ${value}
  <button class="hover:bg-gray-300 hover:text-gray-500 text-gray-300 px-1 rounded-full float-right">
     <span class="mdi mdi-delete"></span>
  </button>
</div>`

export const optionTemplate = value => `<option>${value}</option>`

const navItemTemplate = navigation =>
  `<a href="${navigation.link}" class="block mt-4 lg:inline-block lg:mt-0 text-gray-800 hover:text-white mr-4">
  ${navigation.title}
 </a>`

const navTemplate = () => `<nav class="flex items-center justify-between flex-wrap bg-yellow-500 p-4 relative">
  <div class="flex items-center flex-shrink-0 text-gray-800 w-full">
      <a href="/" class="mr-2">
        <img src="/service/images/logo_small.png" class="w-6">
      </a>
    <div class="flex justify-start">
      <div class="hover:bg-yellow-400 px-2 py-1 rounded">
         <a href="/map.html" class="block inline-block lg:mt-0 text-gray-800 text-sm">
          노선도
          </a>
      </div>
      <div class="hover:bg-yellow-400 px-2 py-1 rounded">
         <a href="/search.html" class="block inline-block lg:mt-0 text-gray-800 text-sm">
          경로 조회
          </a>
      </div>
    </div>
</nav>`

export const subwayLinesTemplate = line =>
  `<div class="border border-gray-200 py-2 px-4 text-gray-800 ">
  <span class="${line.bgColor} w-3 h-3 rounded-full inline-block mr-1"></span>
  ${line.title}
  <button class="hover:bg-gray-300 hover:text-gray-500 text-gray-300 px-1 rounded-full float-right">
     <span class="mdi mdi-delete"></span>
  </button>
</div>`

export const subwayLinesItemTemplate = line => {
  const stationsTemplate = line.stations.map(station => listItemTemplate(station)).join('')
  return `<div class="inline-block w-1/2 px-2">
            <div class="rounded-sm w-full slider-list">
              <div class="border ${line.bgColor} lint-title px-4 py-1">${line.title}</div>
              <div class="overflow-y-auto height-90">
              ${stationsTemplate}
              </div>
            </div>
          </div>`
}

export const edgeItemTemplate = edge => {
  return `<li class="edge-item w-full border border-gray-300 py-2 px-3 text-left text-gray-700">
            <span class="mdi mdi-subway-variant mr-2"></span>
            <span>${edge.departureStation}</span>
            <span class="mdi mdi-arrow-right text-gray-500"></span>
            <span>${edge.arrivalStation}</span>
            <button class="hover:bg-gray-300 hover:text-gray-500 text-gray-300 px-1 rounded-full float-right">
              <span class="mdi mdi-delete"></span>
            </button>
          </li>`
}

export const initNavigation = () => {
  document.querySelector('body').insertAdjacentHTML('afterBegin', navTemplate())
}

export const ErrorAlertTemplate = message => {
  return `<div class="flex justify-center error-alert-container">
            <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded fixed bottom-0" role="alert">
               <strong class="font-bold">${message}</strong>
            </div>
          </div>`
}
