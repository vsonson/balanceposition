(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('IncentiveDeleteController',IncentiveDeleteController);

    IncentiveDeleteController.$inject = ['$uibModalInstance', 'entity', 'Incentive'];

    function IncentiveDeleteController($uibModalInstance, entity, Incentive) {
        var vm = this;

        vm.incentive = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Incentive.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
