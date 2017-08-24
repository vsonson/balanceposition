(function() {
    'use strict';
    angular
        .module('balancepositionApp')
        .factory('PathStep', PathStep);

    PathStep.$inject = ['$resource'];

    function PathStep ($resource) {
        var resourceUrl =  'api/path-steps/:id';

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
