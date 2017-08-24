(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('MetricHistoryDeleteController',MetricHistoryDeleteController);

    MetricHistoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'MetricHistory'];

    function MetricHistoryDeleteController($uibModalInstance, entity, MetricHistory) {
        var vm = this;

        vm.metricHistory = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MetricHistory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
