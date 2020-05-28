const METHOD = {
    GET_WITH_AUTH() {
        return {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                Authorization: localStorage.getItem("jwt") || ""
            }
        };
    },
    PUT(data) {
        return {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                Authorization: localStorage.getItem("jwt") || ""
            },
            body: JSON.stringify({
                ...data
            })
        };
    },
    DELETE(data) {
        return {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json",
                Authorization: localStorage.getItem("jwt") || ""
            },
            body: JSON.stringify({
                ...data
            })
        };
    },
    POST(data) {
        return {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Authorization: localStorage.getItem("jwt") || ""
            },
            body: JSON.stringify({
                ...data
            })
        };
    }
};

const api = (() => {
    const request = (uri, config) => fetch(uri, config);
    const requestWithJsonData = (uri, config) =>
        fetch(uri, config).then(response => {
            if (!response.ok) {
                return;
            }
            return response.json();
        });

    const member = {
        get() {
            return requestWithJsonData(`/members`, METHOD.GET_WITH_AUTH());
        },
        create(newMember) {
            return request(`/members/signup`, METHOD.POST(newMember));
        },
        update(updatedData) {
            return request(`/members`, METHOD.PUT(updatedData));
        },
        delete() {
            return request(`/members`, METHOD.DELETE());
        },
        login(loginInfo) {
            return requestWithJsonData(`/oauth/token`, METHOD.POST(loginInfo));
        }
    };

    const loginMember = {
        get() {
            return requestWithJsonData(`/me/bearer`, METHOD.GET_WITH_AUTH());
        },
        update(updatedInfo) {
            return request(`/me`, METHOD.PUT(updatedInfo));
        },
        delete() {
            return request(`/me`, METHOD.DELETE());
        }
    };

    const line = {
        getAll() {
            return request(`/lines/detail`);
        },
        getAllDetail() {
            return requestWithJsonData(`/lines/detail`);
        }
    };

    const path = {
        find(params) {
            return requestWithJsonData(
                `/paths?source=${params.source}&target=${params.target}&type=${params.type}`
            );
        }
    };

    const favorite = {
        create(favoritePath) {
            return request(`/members/favorites`, METHOD.POST(favoritePath));
        },
        getAll() {
            return requestWithJsonData(`/members/favorites`, METHOD.GET_WITH_AUTH());
        },
        delete(target) {
            return request(`/members/favorites`, METHOD.DELETE(target));
        }
    };

    return {
        member,
        loginMember,
        line,
        path,
        favorite
    };
})();

export default api;
