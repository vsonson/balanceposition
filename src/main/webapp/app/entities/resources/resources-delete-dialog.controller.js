(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('ResourcesDeleteController',ResourcesDeleteController);

    ResourcesDeleteController.$inject = ['$uibModalInstance', 'entity', 'Resources'];

    function ResourcesDeleteController($uibModalInstance, entity, Resources) {
        var vm = this;

        vm.resources = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Resources.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
