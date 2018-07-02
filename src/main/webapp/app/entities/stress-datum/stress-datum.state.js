(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('stress-datum', {
            parent: 'entity',
            url: '/stress-datum',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'StressData'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stress-datum/stress-data.html',
                    controller: 'StressDatumController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('stress-datum-detail', {
            parent: 'stress-datum',
            url: '/stress-datum/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'StressDatum'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stress-datum/stress-datum-detail.html',
                    controller: 'StressDatumDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'StressDatum', function($stateParams, StressDatum) {
                    return StressDatum.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'stress-datum',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('stress-datum-detail.edit', {
            parent: 'stress-datum-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stress-datum/stress-datum-dialog.html',
                    controller: 'StressDatumDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StressDatum', function(StressDatum) {
                            return StressDatum.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stress-datum.new', {
            parent: 'stress-datum',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stress-datum/stress-datum-dialog.html',
                    controller: 'StressDatumDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                value: null,
                                timestamp: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('stress-datum', null, { reload: 'stress-datum' });
                }, function() {
                    $state.go('stress-datum');
                });
            }]
        })
        .state('stress-datum.edit', {
            parent: 'stress-datum',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stress-datum/stress-datum-dialog.html',
                    controller: 'StressDatumDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StressDatum', function(StressDatum) {
                            return StressDatum.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stress-datum', null, { reload: 'stress-datum' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stress-datum.delete', {
            parent: 'stress-datum',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stress-datum/stress-datum-delete-dialog.html',
                    controller: 'StressDatumDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['StressDatum', function(StressDatum) {
                            return StressDatum.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stress-datum', null, { reload: 'stress-datum' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
