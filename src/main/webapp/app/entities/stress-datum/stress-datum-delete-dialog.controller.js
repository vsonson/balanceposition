(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('StressDatumDeleteController',StressDatumDeleteController);

    StressDatumDeleteController.$inject = ['$uibModalInstance', 'entity', 'StressDatum'];

    function StressDatumDeleteController($uibModalInstance, entity, StressDatum) {
        var vm = this;

        vm.stressDatum = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            StressDatum.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
