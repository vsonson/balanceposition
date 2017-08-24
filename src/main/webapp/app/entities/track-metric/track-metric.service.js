(function() {
    'use strict';
    angular
        .module('balancepositionApp')
        .factory('TrackMetric', TrackMetric);

    TrackMetric.$inject = ['$resource'];

    function TrackMetric ($resource) {
        var resourceUrl =  'api/track-metrics/:id';

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
