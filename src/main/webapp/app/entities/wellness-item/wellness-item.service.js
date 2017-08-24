(function() {
    'use strict';
    angular
        .module('balancepositionApp')
        .factory('WellnessItem', WellnessItem);

    WellnessItem.$inject = ['$resource'];

    function WellnessItem ($resource) {
        var resourceUrl =  'api/wellness-items/:id';

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
