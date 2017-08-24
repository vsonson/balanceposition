(function() {
    'use strict';
    angular
        .module('balancepositionApp')
        .factory('KeyPair', KeyPair);

    KeyPair.$inject = ['$resource'];

    function KeyPair ($resource) {
        var resourceUrl =  'api/key-pairs/:id';

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
