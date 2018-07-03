(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('SleepDatumDeleteController',SleepDatumDeleteController);

    SleepDatumDeleteController.$inject = ['$uibModalInstance', 'entity', 'SleepDatum'];

    function SleepDatumDeleteController($uibModalInstance, entity, SleepDatum) {
        var vm = this;

        vm.sleepDatum = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SleepDatum.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
