(function() {
    'use strict';
    angular
        .module('balancepositionApp')
        .factory('PathAction', PathAction);

    PathAction.$inject = ['$resource'];

    function PathAction ($resource) {
        var resourceUrl =  'api/path-actions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
