(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('WellnessItemDeleteController',WellnessItemDeleteController);

    WellnessItemDeleteController.$inject = ['$uibModalInstance', 'entity', 'WellnessItem'];

    function WellnessItemDeleteController($uibModalInstance, entity, WellnessItem) {
        var vm = this;

        vm.wellnessItem = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            WellnessItem.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
