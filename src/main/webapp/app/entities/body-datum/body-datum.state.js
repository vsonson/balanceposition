(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('body-datum', {
            parent: 'entity',
            url: '/body-datum',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'BodyData'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/body-datum/body-data.html',
                    controller: 'BodyDatumController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('body-datum-detail', {
            parent: 'body-datum',
            url: '/body-datum/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'BodyDatum'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/body-datum/body-datum-detail.html',
                    controller: 'BodyDatumDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'BodyDatum', function($stateParams, BodyDatum) {
                    return BodyDatum.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'body-datum',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('body-datum-detail.edit', {
            parent: 'body-datum-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/body-datum/body-datum-dialog.html',
                    controller: 'BodyDatumDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BodyDatum', function(BodyDatum) {
                            return BodyDatum.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('body-datum.new', {
            parent: 'body-datum',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/body-datum/body-datum-dialog.html',
                    controller: 'BodyDatumDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                headache: null,
                                digestive: null,
                                timestamp: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('body-datum', null, { reload: 'body-datum' });
                }, function() {
                    $state.go('body-datum');
                });
            }]
        })
        .state('body-datum.edit', {
            parent: 'body-datum',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/body-datum/body-datum-dialog.html',
                    controller: 'BodyDatumDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BodyDatum', function(BodyDatum) {
                            return BodyDatum.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('body-datum', null, { reload: 'body-datum' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('body-datum.delete', {
            parent: 'body-datum',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/body-datum/body-datum-delete-dialog.html',
                    controller: 'BodyDatumDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['BodyDatum', function(BodyDatum) {
                            return BodyDatum.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('body-datum', null, { reload: 'body-datum' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
