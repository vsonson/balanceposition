(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('BodyDatumDeleteController',BodyDatumDeleteController);

    BodyDatumDeleteController.$inject = ['$uibModalInstance', 'entity', 'BodyDatum'];

    function BodyDatumDeleteController($uibModalInstance, entity, BodyDatum) {
        var vm = this;

        vm.bodyDatum = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            BodyDatum.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
