export const listItemTemplate = data =>
  `<div class="list-item border border-gray-200 py-2 px-4 text-gray-800" data-id="${data.id}">
    ${data.name}
  </div>`;

export const memberInfo = member => {
  if (!member) {
    return ` <a href="/login" class="inline-block login-button absolute text-sm px-4 py-2 leading-none border rounded text-gray-800 border-gray-800 hover:border-transparent hover:bg-yellow-400">로그인</a>`;
  }
  return `<div class="dropdown inline-block absolute right-10px">
      <button class="text-gray-800 border-gray-800 hover:border-transparent hover:bg-yellow-400 font-semibold py-2 px-5 rounded inline-flex items-center">
        <span class="mr-1">${
          member && member.name ? member.name : "이름 없음"
        }</span>
        <svg class="fill-current h-4 w-4" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20">
          <path d="M9.293 12.95l.707.707L15.657 8l-1.414-1.414L10 10.828 5.757 6.586 4.343 8z"/> 
        </svg>
      </button>
      <ul class="dropdown-menu absolute hidden text-gray-700 bg-white shadow rounded z-10">
        <li>
          <a href="/mypage" class="rounded-t hover:bg-gray-400 py-3 px-5 block whitespace-no-wrap">
            <span class="mdi mdi-account mr-1"></span>
            나의정보
          </a>
        </li>
        <li>
          <a href="/favorites" class="hover:bg-gray-400 py-3 px-5 block whitespace-no-wrap">
            <span class="mdi mdi-star mr-1"></span>
            즐겨찾기
          </a>
        </li>
        <li>
          <a href="/logout" class="rounded-b hover:bg-gray-400 py-3 px-5 block whitespace-no-wrap">
          <span class="mdi mdi-logout mr-1"></span>
          로그아웃
          </a>
        </li>
      </ul>
    </div>`;
};

export const navTemplate = member => `<nav class="flex items-center justify-between flex-wrap bg-yellow-500 p-4 relative">
  <div class="w-full block flex-grow lg:flex lg:items-center lg:w-auto">
    <div class="text-sm inline-block lg:flex-grow">
      <div class="px-2 inline-block relative top-5px">
        <a href="/" class="mr-2 inline-block">
          <img src="/service/images/logo_small.png" class="w-6">
        </a>
      </div>
      <div class="hover:bg-yellow-400 px-2 py-1 rounded inline-block">
         <a href="/map" class="text-gray-800 text-sm">
          노선도
          </a>
      </div>
      <div class="hover:bg-yellow-400 px-2 py-1 rounded inline-block">
         <a href="/search" class="text-gray-800 text-sm">
          경로 조회
          </a>
      </div>
    </div>
    ${memberInfo(member)}
  </div>
</nav>`;

export const subwayLinesItemTemplate = line => {
  const stations = line.stations
    ? line.stations.map(station => listItemTemplate(station)).join("")
    : null;
  return `<div class="inline-block w-1/2 px-2">
            <div class="rounded-sm w-full slider-list">
              <div class="border ${
                line.bgColor ? line.bgColor : ""
              } lint-name px-4 py-1">${line.name}</div>
              <div class="overflow-y-auto height-90">
              ${stations}
              </div>
            </div>
          </div>`;
};

export const searchResultTemplate = result => {
  const lastIndex = result.stations.length - 1;
  const pathResultTemplate = result.stations
    .map((station, index) =>
      pathStationTemplate(station.name, index, lastIndex)
    )
    .join("");
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
    </div>`;
};

export const pathStationTemplate = (name, index, lastIndex) => {
  return `
  ${
    index === 0 || index === lastIndex
      ? `${
          index === lastIndex
            ? `<span class="mdi mdi-arrow-right-bold text-gray-500"></span>`
            : ``
        }
        <span class="font-bold">${name}</span>`
      : `<span class="mdi mdi-arrow-right-bold text-gray-500"></span>
         <span class="text-gray-600">${name}</span>
        `
  }`;
};

export const initNavigation = member => {
  document
    .querySelector("body")
    .insertAdjacentHTML("afterBegin", navTemplate(member));
};

export const edgeItemTemplate = edge => {
    return `<li class="edge-item w-full border border-gray-300 py-2 px-3 text-left text-gray-700">
            <span class="mdi mdi-subway-variant mr-2"></span>
            <span class="edge-item-source">${edge.source}</span>
            <span class="mdi mdi-arrow-right text-gray-500"></span>
            <span class="edge-item-target">${edge.target}</span>
            <button class="hover:bg-gray-300 hover:text-gray-500 text-gray-300 px-1 rounded-full float-right">
              <span class="mdi mdi-delete"></span>
            </button>
          </li>`;
};
