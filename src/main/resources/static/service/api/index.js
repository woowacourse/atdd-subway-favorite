export const setAccessTokenCookie = (accessToken) => {
    document.cookie = 'accessToken=' + accessToken + ';path=/';
};

const getAccessTokenCookie = () => {
    if (document.cookie) {
        const array = document.cookie.split('accessToken=');
        if (array.length >= 2) {
            const arraySub = array[1].split(';');
            return arraySub[0];
        }
    }
    return null;
};

const METHOD = {
    GET() {
        return {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + getAccessTokenCookie()
            }
        }
    },
    PUT(data) {
        return {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + getAccessTokenCookie()
            },
            body: JSON.stringify({
                ...data
            })
        }
    },
    DELETE() {
        return {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + getAccessTokenCookie()
            }
        }
    },
    DELETE_DATA(data) {
        return {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + getAccessTokenCookie()
            },
            body: JSON.stringify({
                ...data
            })
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
    },
    POST_WITH_AUTH(data) {
        return {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + getAccessTokenCookie()
            },
            body: JSON.stringify({
                ...data
            })
        }
    }
};

const resolver = (response) => {
    return new Promise((resolve, reject) => {
        response.status < 400 ? resolve(response) : reject(response);
    });
};

const api = (() => {
    const request = (uri, config) => fetch(uri, config).then(resolver);
    const requestWithJsonData = (uri, config) => fetch(uri, config).then(data => data.json());

    const line = {
        getAll() {
            return request(`/lines/detail`)
        },
        getAllDetail() {
            return requestWithJsonData(`/lines/detail`)
        }
    };

    const path = {
        find(params) {
            return requestWithJsonData(`/paths?source=${params.source}&target=${params.target}&type=${params.type}`)
        }
    };

    const member = {
        create(data) {
            return request("/members", METHOD.POST(data));
        },
        login(data) {
            return request("oauth/token", METHOD.POST(data));
        },
        get() {
            return request("/members/me", METHOD.GET());
        },
        update(data) {
            return request("/members/me", METHOD.PUT(data));
        },
        delete() {
            return request("/members/me", METHOD.DELETE());
        }
    };

    const favorite = {
        create(data) {
            return request("/favorites/me", METHOD.POST_WITH_AUTH(data));
        },
        get() {
            return request("/favorites/me", METHOD.GET());
        },
        delete(data) {
            return request("/favorites/me", METHOD.DELETE_DATA(data));
        }
    };

    return {
        line,
        path,
        member,
        favorite
    }
})();

export default api
