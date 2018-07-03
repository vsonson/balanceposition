(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('PerformanceDatumDeleteController',PerformanceDatumDeleteController);

    PerformanceDatumDeleteController.$inject = ['$uibModalInstance', 'entity', 'PerformanceDatum'];

    function PerformanceDatumDeleteController($uibModalInstance, entity, PerformanceDatum) {
        var vm = this;

        vm.performanceDatum = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PerformanceDatum.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
