(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('FocusDatumDeleteController',FocusDatumDeleteController);

    FocusDatumDeleteController.$inject = ['$uibModalInstance', 'entity', 'FocusDatum'];

    function FocusDatumDeleteController($uibModalInstance, entity, FocusDatum) {
        var vm = this;

        vm.focusDatum = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            FocusDatum.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
