(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('TrackMetricDeleteController',TrackMetricDeleteController);

    TrackMetricDeleteController.$inject = ['$uibModalInstance', 'entity', 'TrackMetric'];

    function TrackMetricDeleteController($uibModalInstance, entity, TrackMetric) {
        var vm = this;

        vm.trackMetric = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TrackMetric.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
