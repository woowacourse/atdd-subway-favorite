const METHOD = {
    PUT() {
        return {
            method: 'PUT'
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

    const line = {
        getAll() {
            return request(`/lines/detail`)
        },
        getAllDetail() {
            return requestWithJsonData(`/lines/detail`)
        }
    }

    const path = {
        find(params) {
            return requestWithJsonData(`/paths?source=${params.source}&target=${params.target}&type=${params.type}`)
        }
    }

    return {
        line,
        path
    }
})()

export default api
