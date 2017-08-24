(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('MetricHistoryDetailController', MetricHistoryDetailController);

    MetricHistoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'MetricHistory', 'UserInfo', 'TrackMetric', 'TrackMetricQuestion'];

    function MetricHistoryDetailController($scope, $rootScope, $stateParams, previousState, entity, MetricHistory, UserInfo, TrackMetric, TrackMetricQuestion) {
        var vm = this;

        vm.metricHistory = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('balancepositionApp:metricHistoryUpdate', function(event, result) {
            vm.metricHistory = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
