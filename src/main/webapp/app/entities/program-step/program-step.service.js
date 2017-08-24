(function() {
    'use strict';
    angular
        .module('balancepositionApp')
        .factory('ProgramStep', ProgramStep);

    ProgramStep.$inject = ['$resource'];

    function ProgramStep ($resource) {
        var resourceUrl =  'api/program-steps/:id';

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
