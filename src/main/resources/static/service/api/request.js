export const request = (uri, config) => fetch(uri, config).then(async response => {
  if (response.ok) {
    return response
  }
  return Promise.reject()
})

export const requestWithJsonData = (uri, config) => request(uri, config).then(data => data.json())
