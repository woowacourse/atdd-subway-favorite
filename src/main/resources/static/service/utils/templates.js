export const listItemTemplate = data =>
  `<div class="list-item border border-gray-200 py-2 px-4 text-gray-800" data-id="${data.id}">
    ${data.name}
  </div>`

export const navTemplate = `<nav class="flex items-center justify-between flex-wrap bg-yellow-500 p-4 relative">
  <div class="flex items-center flex-shrink-0 text-gray-800 w-full">
      <a href="/" class="mr-2">
        <img src="/service/images/logo_small.png" class="w-6">
      </a>
    <div class="flex justify-start">
      <div class="hover:bg-yellow-400 px-2 py-1 rounded">
         <a href="/map" class="block inline-block lg:mt-0 text-gray-800 text-sm">
          노선도
          </a>
      </div>
      <div class="hover:bg-yellow-400 px-2 py-1 rounded">
         <a href="/search" class="block inline-block lg:mt-0 text-gray-800 text-sm">
          경로 조회
          </a>
      </div>
    </div>
</nav>`

export const subwayLinesItemTemplate = line => {
  const stations = line.stations ? line.stations.map(station => listItemTemplate(station))
  .join('') : null
  return `<div class="inline-block w-1/2 px-2">
            <div class="rounded-sm w-full slider-list">
              <div class="border ${line.bgColor ? line.bgColor : ''} lint-name px-4 py-1">${line.name}</div>
              <div class="overflow-y-auto height-90">
              ${stations}
              </div>
            </div>
          </div>`
}

export const searchResultTemplate = result => {
  const lastIndex = result.stations.length - 1
  const pathResultTemplate = result.stations.map((station, index) => pathStationTemplate(station.name,
    index,
    lastIndex)).join('')
  return `<div class="px-2 py-4 border-b">
      <div class="w-full flex mb-3">
        <div class="inline-block w-1/2 border-r text-center">
          <div class="text-gray-600 text-sm">소요시간</div>
          <div>${result.duration}분</div>
        </div>
        <div class="inline-block w-1/2 text-center">
          <div class="text-gray-600 text-sm">거리</div>
          <div>${result.distance}km</div>
        </div>
      </div>
    </div>
    <div class="relative pt-3 pb-10">
      <div class="px-2 py-1 w-full flex">
        <div class="w-10/12 inline-block">
          ${pathResultTemplate}
        </div>
      </div>
    </div>`
}

export const pathStationTemplate = (name, index, lastIndex) => {
  return `
  ${
    index === 0 || index === lastIndex
      ? `${index === lastIndex ? `<span class="mdi mdi-arrow-right-bold text-gray-500"></span>` : ``}
        <span class="font-bold">${name}</span>`
      : `<span class="mdi mdi-arrow-right-bold text-gray-500"></span>
         <span class="text-gray-600">${name}</span>
        `
  }`
}

export const initNavigation = () => {
  document.querySelector('body').insertAdjacentHTML('afterBegin', navTemplate)
}
