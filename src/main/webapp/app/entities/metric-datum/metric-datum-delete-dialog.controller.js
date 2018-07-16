(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('MetricDatumDeleteController',MetricDatumDeleteController);

    MetricDatumDeleteController.$inject = ['$uibModalInstance', 'entity', 'MetricDatum'];

    function MetricDatumDeleteController($uibModalInstance, entity, MetricDatum) {
        var vm = this;

        vm.metricDatum = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MetricDatum.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
