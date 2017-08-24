(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('TrackMetricDetailController', TrackMetricDetailController);

    TrackMetricDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'TrackMetric', 'TrackMetricQuestion'];

    function TrackMetricDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, TrackMetric, TrackMetricQuestion) {
        var vm = this;

        vm.trackMetric = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('balancepositionApp:trackMetricUpdate', function(event, result) {
            vm.trackMetric = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
