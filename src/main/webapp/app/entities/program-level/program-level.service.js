(function() {
    'use strict';
    angular
        .module('balancepositionApp')
        .factory('ProgramLevel', ProgramLevel);

    ProgramLevel.$inject = ['$resource'];

    function ProgramLevel ($resource) {
        var resourceUrl =  'api/program-levels/:id';

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
