const METHOD = {
  PUT(data) {
    return {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        ...data
      })
    }
  },
  DELETE() {
    return {
      method: 'DELETE'
    }
  },
  POST(data) {
    return {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        ...data
      })
    }
  }
}

const api = (() => {
  const request = (uri, config) => fetch(uri, config)
  const requestWithJsonData = (uri, config) => fetch(uri, config).then(data => data.json())

  const station = {
    get(id) {
      return requestWithJsonData(`/stations/${id}`)
    },
    getAll() {
      return requestWithJsonData(`/stations`)
    },
    create(data) {
      return requestWithJsonData(`/stations`, METHOD.POST(data))
    },
    update(data, id) {
      return requestWithJsonData(`/stations/${id}`, METHOD.PUT(data))
    },
    delete(id) {
      return request(`/stations/${id}`, METHOD.DELETE())
    }
  }

  const line = {
    get(id) {
        return requestWithJsonData(`/admin/lines/${id}`)
    },
    getAll() {
        return requestWithJsonData(`/admin/lines`)
    },
    getAllDetail() {
        return requestWithJsonData(`/admin/lines/detail`)
    },
    addLineStation(lineId, lineStationCreateRequestView) {
        return request(`/admin/lines/${lineId}/stations`, METHOD.POST(lineStationCreateRequestView))
    },
    create(data) {
        return requestWithJsonData(`/admin/lines`, METHOD.POST(data))
    },
    update(id, data) {
        return request(`/admin/lines/${id}`, METHOD.PUT(data))
    },
    deleteLineStation(lineId, stationId) {
        return request(`/admin/lines/${lineId}/stations/${stationId}`, METHOD.DELETE())
    },
    delete(id) {
        return request(`/admin/lines/${id}`, METHOD.DELETE())
    }
  }

  return {
    station,
    line
  }
})()

export default api
